package com.keren.tomer.minesweeper


import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.GridLayout
import com.fondesa.kpermissions.extension.onAccepted
import com.fondesa.kpermissions.extension.permissionsBuilder
import java.io.File


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
        Log.i(TAG, "Created from $activity")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val title = when (result!!) {
            Game.EndState.WON -> "You won!"
            Game.EndState.LOST -> "You lost :("
            Game.EndState.UNDECIDED -> "You... huh"
        }
        return activity?.let { activity: FragmentActivity ->
            AlertDialog.Builder(activity).apply {
                setMessage(title)
                setPositiveButton("Play again") { dialog, which ->
                    activity.restart()
                }
                setNeutralButton("Share result") { _, _ ->
                    val board = activity.findViewById<GridLayout>(R.id.game_board)
                    board?.screenshot?.share(activity)
                }
                setNegativeButton("Change Difficulty") { dialog, which ->
                    activity.finish()
                }
            }.create()
        } ?: throw IllegalStateException("Activity cannot be null")
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

fun Bundle.putEnum(key: String, enum: Enum<*>) {
    putString(key, enum.name)
}

inline fun <reified T : Enum<T>> Bundle.getEnum(key: String): T {
    return enumValueOf(getString(key)!!)
}

val View.screenshot: Bitmap
    get() {
        return Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444).apply {
            val canvas = Canvas(this)
            val bgDrawable = background
            if (bgDrawable != null) {
                bgDrawable.draw(canvas)
            } else {
                canvas.drawColor(Color.WHITE)
            }
            draw(canvas)
        }
    }

/**
 * based off of https://stackoverflow.com/questions/17160593/how-to-attach-a-bitmap-when-launching-action-send-intent
 */
fun Bitmap.share(context: Activity) {
    val permissionBuilder = context.permissionsBuilder(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE).build()
    permissionBuilder.onAccepted {
        val dir = File("${Environment.getExternalStorageDirectory().absolutePath}${File.separator}minesweeper").apply {
            mkdirs()
        }
        val temp_file = File(dir, "share_screenshot.jpeg")
        temp_file.outputStream().use { out ->
            // open/close outputStream, see https://stackoverflow.com/questions/35444264/how-do-i-write-to-a-file-in-kotlin/35462184#35462184
            compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
        }
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_STREAM, Uri.parse(temp_file.path)) // workaround to creating a FileProvider https://stackoverflow.com/a/52925917/4342751
            type = "image/jpeg"
        }
        context.startActivity(Intent.createChooser(shareIntent, context.resources.getText(R.string.send_to)))
    }
    permissionBuilder.send()
}