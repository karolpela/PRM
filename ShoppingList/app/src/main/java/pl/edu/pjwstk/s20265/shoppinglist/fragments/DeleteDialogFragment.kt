package pl.edu.pjwstk.s20265.shoppinglist.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import pl.edu.pjwstk.s20265.shoppinglist.R

class DeleteDialogFragment : DialogFragment() {

    internal lateinit var listener: DeleteDialogListener

    interface DeleteDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as DeleteDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(("$context must implement NoticeDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(context)
            .setTitle(context?.getString(R.string.list_dialog_delete_title))
            .setMessage(context?.getString(R.string.list_dialog_delete_message))
            .setPositiveButton(context?.getString(R.string.dialog_button_delete)) { _, _ ->
                listener.onDialogPositiveClick(this)
            }
            .setNegativeButton(context?.getString(R.string.dialog_button_cancel)) { _, _ ->
                listener.onDialogNegativeClick(this)
            }
            .create()

    companion object {
        const val TAG = "DeleteDialog"
    }
}
