package kyrylost.apps.eatwise.customviews

import android.content.Context
import android.util.AttributeSet
import android.widget.RadioGroup

class CustomRadioGroup @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : RadioGroup(context, attrs) {

    var setCheckedItemAt: Int? = null
        set(value) {
            field = value
            if (value != null) {
                val checkedChildId = this.getChildAt(value).id
                this.check(checkedChildId)
            }
        }

    fun getCheckedItemNumber(): Int {
        val checkedRadioButtonId = checkedRadioButtonId
        return indexOfChild(findViewById(checkedRadioButtonId))
    }

}