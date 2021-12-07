package com.solution.edittext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.EditText
import android.widget.TextView
import com.solution.edittext.databinding.ActivityMainBinding
import java.lang.Exception
import java.lang.StringBuilder
import java.text.DecimalFormat

enum class eConvertType { DECIMAL, STRING }

class MainActivity : AppCompatActivity() {
    // # Variable
    private var BINDING: ActivityMainBinding? = null
    private val mBinding get() = BINDING!!
    private lateinit var mConvertType: eConvertType
    private var mBackspaceDetector: Boolean = false
    private lateinit var mEt: EditText
    private lateinit var mTv: TextView

    // # Life Cycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Init()
    }

    // # Essential Function
    private val Init = {
        // View Binding
        run {
            BINDING = ActivityMainBinding.inflate(layoutInflater)
            setContentView(mBinding.root)
        }

        // Caching
        run {
            mEt = mBinding.et
            mTv = mBinding.tvType
        }

        // Values
        run {
            mConvertType = eConvertType.DECIMAL
            mBackspaceDetector = false
            mTv.text = "${mConvertType} Type"
        }

        // Events
        run {
            // EditText View에 TextWatcher 이벤트 등록
            mEt.addTextChangedListener(textWatcher)

            // 컨버팅 방식 변경 이벤트
            mBinding.btnDecimal.setOnClickListener {
                mConvertType = eConvertType.DECIMAL
                mTv.text = "${mConvertType} Type"
                mEt.text.clear()
            }
            mBinding.btnString.setOnClickListener {
                mConvertType = eConvertType.STRING
                mTv.text = "${mConvertType} Type"
                mEt.text.clear()
            }

            // 지우기 버튼 이벤트
            mBinding.btnClear.setOnClickListener { mEt.text.clear() }

            // 키 입력 이벤트
            mEt.setOnKeyListener { v, keyCode, event ->
                mBackspaceDetector = keyCode == KeyEvent.KEYCODE_DEL
                false
            }
        }
    }

    // # Definition Function
    private val textWatcher = object : TextWatcher {
        var beforeLength = 0
        var afterLength = 0
        var selectionPosition = 0

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeLength = s.toString().length
            selectionPosition = mEt.selectionStart
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        override fun afterTextChanged(s: Editable?) {
            if (s.toString().isNotEmpty()) {
                // 무한루프를 막기 위해 이벤트 리스너 임시 제거
                mEt.removeTextChangedListener(this)

                val convertValue = SolutionLib.RemoveReplace(s.toString(), ",")

                when (mConvertType) {
                    eConvertType.DECIMAL -> mEt.setText(DecimalConverter.Append(convertValue))
                    eConvertType.STRING -> mEt.setText(StringConverter.Append(convertValue))
                }

                afterLength = mEt.text.length
                FixedSelection(beforeLength, afterLength, selectionPosition)

                if (mBackspaceDetector && afterLength - beforeLength == 0) {
                    when (mConvertType) {
                        eConvertType.DECIMAL -> mEt.setText(DecimalConverter.CommaDelete(mEt.text.toString(), selectionPosition))
                        eConvertType.STRING -> mEt.setText(StringConverter.CommaDelete(mEt.text.toString(), selectionPosition))
                    }

                    afterLength = mEt.text.length
                    FixedSelection(beforeLength, afterLength, selectionPosition)
                }

                // 제거했던 이벤트 리스너 재등록
                mEt.addTextChangedListener(this)
            }
        }
    }

    // 커서 포지션 고정
    private val FixedSelection: (Int, Int, Int) -> Unit = { beforeLength, afterLength, selectionPosition ->
        val positionOffset = afterLength - beforeLength
        val resultPosition = selectionPosition + positionOffset

        if (resultPosition > 0) mEt.setSelection(resultPosition)
        else mEt.setSelection(0)

    }

    // 숫자 방식
    class DecimalConverter {
        companion object {
            val Append: (String) -> String = { value ->
                DecimalFormat("###,###").format(value.toDouble())
            }

            val CommaDelete: (String, Int) -> String = { value, position ->
                val sb = StringBuilder(value).deleteCharAt(position - 2)
                val convertValue = SolutionLib.RemoveReplace(sb.toString(), ",")
                DecimalFormat("###,###").format(convertValue.toDouble())
            }
        }
    }

    // 문자열 방식
    class StringConverter {
        companion object {
            val Append: (String) -> String = { value ->
                val sb = StringBuilder(value)
                var num = 3

                // 0번째 값에 0 값이 들어가지 않도록 해주며 경우에 따라 0번째 값이 0이 될 경우 0번재 값이 0이 아닐 때 까지 0번째 값을 반복해서 삭제시킴
                try {
                    while (sb[0].toString() == "0") sb.deleteCharAt(0)
                } catch (e: Exception) {
                }

                // 자릿수 마다 콤마 삽입
                while (num < sb.length) {
                    sb.insert(sb.length - num, ",")
                    num += 4
                }

                sb.toString()
            }

            val CommaDelete: (String, Int) -> String = { value, position ->
                val sb = StringBuilder(value).deleteCharAt(position - 2)
                val convertValue = SolutionLib.RemoveReplace(sb.toString(), ",")
                Append(convertValue)
            }
        }
    }
}