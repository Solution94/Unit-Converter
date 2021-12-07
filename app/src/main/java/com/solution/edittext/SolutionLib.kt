/*
----------------------------------------------------------------------------------------------------
#Copyright - Solution
#Blog - https://solution94.tistory.com/
#Creation Date - 2021-11-10

#Note
[2021-11-10]
* Created Method > Toast
* Created Method > Log

[2021-11-??]
* Created Method > GetResolution

[2021-12-07]
* Created Method > RemoveReplace
----------------------------------------------------------------------------------------------------
 */

package com.solution.edittext

import android.content.Context
import android.view.Display
import java.util.*

import android.view.WindowManager
import java.lang.StringBuilder
import kotlin.collections.HashMap


class SolutionLib {
    companion object {
        /**
         * Toast Message를 출력한다.
         *
         * @param context 표시 Context (:Context)
         * @param objects 메시지 (:Any)
         */
        val Toast = { context: Context, objects: Any ->
            android.widget.Toast.makeText(
                context,
                objects.toString(),
                android.widget.Toast.LENGTH_SHORT
            ).show()
        }

        /**
         * Debug Log를 출력한다.
         *
         * @param objects 메시지 (:Any)
         */
        val Log = { objects: Any ->
            android.util.Log.d("SOL_LOG", objects.toString())
        }

        /**
         * 실행 중인 기기의 해상도를 읽어온다
         *
         * @param context
         * @return Display Type
         */
        val GetResolution: (context: Context) -> Display = {
            (it.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        }

        /**
         * 전달받은 문자열에서 요청받은 문자열을 제거하고 반환한다
         *
         * @param value 문자열
         * @param replaceTarget 제거할 문자열
         * @return String Type
         */
        val RemoveReplace: (value: String, replaceTarget: String) -> String = { value, replaceTarget ->
            value.replace(replaceTarget, "")
        }
    }
}
