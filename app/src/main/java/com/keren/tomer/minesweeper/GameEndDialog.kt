package com.keren.tomer.minesweeper


import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GameEndDialog.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class GameEndDialog : DialogFragment() {
    private var result: Game.EndState? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            result = it.getEnum<Game.EndState>(RESULT)
        }
        Log.i(TAG,"Created from $activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_end_dialog, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = when(result!!)
        {
            Game.EndState.WON -> "You won!"
            Game.EndState.LOST -> "You lost :("
            Game.EndState.UNDECIDED -> "You... huh"
        }
        return activity?.let {
           AlertDialog.Builder(it).apply {
               setMessage(title)
               setPositiveButton("Play again") {dialog, which ->
                   //TODO: Re-create the activity
               }
               setNeutralButton("Share result"){dialog, which ->
                   //TODO: Share an intent with the time (see issue #12) and a picture of the board
               }
               setNegativeButton("Change Difficulty"){dialog, which ->
                   //TODO: Go back to the home activity
               }
           }.create()
        }?:throw IllegalStateException("Activity cannot be null")
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GameEndDialog.
         */
        // TODO: Rename and change types and number of parameters
        private val RESULT = "result"
        @JvmStatic
        fun newInstance(result: Game.EndState) =
                GameEndDialog().apply {
                    arguments = Bundle().apply {
                        putEnum(RESULT, result)
                    }
                }

        const val TAG = "GameEndDialog"
    }
}

// Dunno if there's a better way to extend both bundle and intents, but
// you probably can extend intents in the same way

fun Bundle.putEnum(key:String, enum: Enum<*>){
    putString(key, enum.name)
}

inline fun <reified T: Enum<T>> Bundle.getEnum(key:String): T {
    return enumValueOf(getString(key)!!)
}
