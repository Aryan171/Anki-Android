/*
 *  Copyright (c) 2024 Brayan Oliveira <brayandso.dev@gmail.com>
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 3 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ichi2.anki.preferences.reviewer

import android.content.Context
import android.content.SharedPreferences
import android.view.KeyEvent
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.ichi2.anki.CollectionManager.TR
import com.ichi2.anki.Flag
import com.ichi2.anki.R
import com.ichi2.anki.preferences.reviewer.MenuDisplayType.ALWAYS
import com.ichi2.anki.preferences.reviewer.MenuDisplayType.DISABLED
import com.ichi2.anki.preferences.reviewer.MenuDisplayType.MENU_ONLY
import com.ichi2.anki.reviewer.Binding
import com.ichi2.anki.reviewer.Binding.AppDefinedModifierKeys
import com.ichi2.anki.reviewer.Binding.ModifierKeys
import com.ichi2.anki.reviewer.Binding.ModifierKeys.Companion.ctrl
import com.ichi2.anki.reviewer.Binding.ModifierKeys.Companion.shift
import com.ichi2.anki.reviewer.CardSide
import com.ichi2.anki.reviewer.MappableAction
import com.ichi2.anki.reviewer.ReviewerBinding
import com.ichi2.anki.ui.internationalization.toSentenceCase

/**
 * @param menuId menu Id of the action
 *
 * @param defaultDisplayType the default display type of the action in the toolbar.
 * Use `null` if the action is restricted to gestures/controls and shouldn't be in the menu,
 * or if the item has a [parentMenu].
 */
enum class ViewerAction(
    @IdRes val menuId: Int = 0,
    @DrawableRes val drawableRes: Int? = null,
    @StringRes val titleRes: Int = R.string.empty_string,
    val defaultDisplayType: MenuDisplayType? = null,
    val parentMenu: ViewerAction? = null,
) : MappableAction<ReviewerBinding> {
    // Always
    UNDO(R.id.action_undo, R.drawable.ic_undo_white, R.string.undo, ALWAYS),

    // Menu only
    REDO(R.id.action_redo, R.drawable.ic_redo, R.string.redo, MENU_ONLY),
    FLAG_MENU(R.id.action_flag, R.drawable.ic_flag_transparent, R.string.menu_flag, MENU_ONLY),
    MARK(R.id.action_mark, R.drawable.ic_star, R.string.menu_mark_note, MENU_ONLY),
    EDIT(R.id.action_edit_note, R.drawable.ic_mode_edit_white, R.string.cardeditor_title_edit_card, MENU_ONLY),
    BURY_MENU(R.id.action_bury, R.drawable.ic_flip_to_back_white, R.string.menu_bury, MENU_ONLY),
    SUSPEND_MENU(R.id.action_suspend, R.drawable.ic_suspend, R.string.menu_suspend, MENU_ONLY),
    DELETE(R.id.action_delete, R.drawable.ic_delete_white, R.string.menu_delete_note, MENU_ONLY),

    // Disabled
    DECK_OPTIONS(R.id.action_deck_options, R.drawable.ic_tune_white, R.string.menu__deck_options, DISABLED),
    CARD_INFO(R.id.action_card_info, R.drawable.ic_dialog_info, R.string.card_info_title, DISABLED),
    ADD_NOTE(R.id.action_add_note, R.drawable.ic_add, R.string.menu_add_note, DISABLED),
    TAG(R.id.action_edit_tags, R.drawable.ic_tag, R.string.menu_edit_tags, DISABLED),
    RESCHEDULE_NOTE(R.id.action_set_due_date, R.drawable.ic_reschedule, titleRes = R.string.empty_string, DISABLED),
    TOGGLE_AUTO_ADVANCE(R.id.action_toggle_auto_advance, R.drawable.ic_fast_forward, R.string.toggle_auto_advance, DISABLED),
    USER_ACTION_1(R.id.user_action_1, R.drawable.user_action_1, R.string.user_action_1, DISABLED),
    USER_ACTION_2(R.id.user_action_2, R.drawable.user_action_2, R.string.user_action_2, DISABLED),
    USER_ACTION_3(R.id.user_action_3, R.drawable.user_action_3, R.string.user_action_3, DISABLED),
    USER_ACTION_4(R.id.user_action_4, R.drawable.user_action_4, R.string.user_action_4, DISABLED),
    USER_ACTION_5(R.id.user_action_5, R.drawable.user_action_5, R.string.user_action_5, DISABLED),
    USER_ACTION_6(R.id.user_action_6, R.drawable.user_action_6, R.string.user_action_6, DISABLED),
    USER_ACTION_7(R.id.user_action_7, R.drawable.user_action_7, R.string.user_action_7, DISABLED),
    USER_ACTION_8(R.id.user_action_8, R.drawable.user_action_8, R.string.user_action_8, DISABLED),
    USER_ACTION_9(R.id.user_action_9, R.drawable.user_action_9, R.string.user_action_9, DISABLED),

    // Child items
    BURY_NOTE(R.id.action_bury_note, drawableRes = null, titleRes = R.string.menu_bury_note, parentMenu = BURY_MENU),
    BURY_CARD(R.id.action_bury_card, drawableRes = null, titleRes = R.string.menu_bury_card, parentMenu = BURY_MENU),
    SUSPEND_NOTE(R.id.action_suspend_note, drawableRes = null, titleRes = R.string.menu_suspend_note, parentMenu = SUSPEND_MENU),
    SUSPEND_CARD(R.id.action_suspend_card, drawableRes = null, titleRes = R.string.menu_suspend_card, parentMenu = SUSPEND_MENU),
    UNSET_FLAG(Flag.NONE.id, Flag.NONE.drawableRes, parentMenu = FLAG_MENU),
    FLAG_RED(Flag.RED.id, Flag.RED.drawableRes, parentMenu = FLAG_MENU),
    FLAG_ORANGE(Flag.ORANGE.id, Flag.ORANGE.drawableRes, parentMenu = FLAG_MENU),
    FLAG_BLUE(Flag.BLUE.id, Flag.BLUE.drawableRes, parentMenu = FLAG_MENU),
    FLAG_GREEN(Flag.GREEN.id, Flag.GREEN.drawableRes, parentMenu = FLAG_MENU),
    FLAG_PINK(Flag.PINK.id, Flag.PINK.drawableRes, parentMenu = FLAG_MENU),
    FLAG_TURQUOISE(Flag.TURQUOISE.id, Flag.TURQUOISE.drawableRes, parentMenu = FLAG_MENU),
    FLAG_PURPLE(Flag.PURPLE.id, Flag.PURPLE.drawableRes, parentMenu = FLAG_MENU),

    // Command only
    SHOW_ANSWER,
    FLIP_OR_ANSWER_EASE1,
    FLIP_OR_ANSWER_EASE2,
    FLIP_OR_ANSWER_EASE3,
    FLIP_OR_ANSWER_EASE4,
    TOGGLE_FLAG_RED,
    TOGGLE_FLAG_ORANGE,
    TOGGLE_FLAG_GREEN,
    TOGGLE_FLAG_BLUE,
    TOGGLE_FLAG_PINK,
    TOGGLE_FLAG_TURQUOISE,
    TOGGLE_FLAG_PURPLE,
    SHOW_HINT,
    SHOW_ALL_HINTS,
    EXIT,
    ;

    override val preferenceKey: String get() = "binding_$name"

    override fun getBindings(prefs: SharedPreferences): List<ReviewerBinding> {
        val prefValue = prefs.getString(preferenceKey, null) ?: return defaultBindings
        return ReviewerBinding.fromPreferenceString(prefValue)
    }

    private val defaultBindings: List<ReviewerBinding> get() =
        when (this) {
            UNDO -> listOf(keycode(KeyEvent.KEYCODE_Z, ctrl()))
            REDO -> listOf(keycode(KeyEvent.KEYCODE_Z, ModifierKeys(shift = true, ctrl = true, alt = false)))
            MARK -> listOf(unicode('*'))
            EDIT -> listOf(keycode(KeyEvent.KEYCODE_E))
            ADD_NOTE -> listOf(keycode(KeyEvent.KEYCODE_A))
            BURY_NOTE -> listOf(unicode('='))
            BURY_CARD -> listOf(unicode('-'))
            SUSPEND_NOTE -> listOf(unicode('!'))
            SUSPEND_CARD -> listOf(unicode('@'))
            TOGGLE_AUTO_ADVANCE -> listOf(keycode(KeyEvent.KEYCODE_A, shift()))
            SHOW_HINT -> listOf(keycode(KeyEvent.KEYCODE_H))
            SHOW_ALL_HINTS -> listOf(keycode(KeyEvent.KEYCODE_G))
            TOGGLE_FLAG_RED ->
                listOf(
                    keycode(KeyEvent.KEYCODE_1, ctrl()),
                    keycode(KeyEvent.KEYCODE_NUMPAD_1, ctrl()),
                )
            TOGGLE_FLAG_ORANGE ->
                listOf(
                    keycode(KeyEvent.KEYCODE_2, ctrl()),
                    keycode(KeyEvent.KEYCODE_NUMPAD_2, ctrl()),
                )
            TOGGLE_FLAG_GREEN ->
                listOf(
                    keycode(KeyEvent.KEYCODE_3, ctrl()),
                    keycode(KeyEvent.KEYCODE_NUMPAD_3, ctrl()),
                )
            TOGGLE_FLAG_BLUE ->
                listOf(
                    keycode(KeyEvent.KEYCODE_4, ctrl()),
                    keycode(KeyEvent.KEYCODE_NUMPAD_4, ctrl()),
                )
            TOGGLE_FLAG_PINK ->
                listOf(
                    keycode(KeyEvent.KEYCODE_5, ctrl()),
                    keycode(KeyEvent.KEYCODE_NUMPAD_5, ctrl()),
                )
            TOGGLE_FLAG_TURQUOISE ->
                listOf(
                    keycode(KeyEvent.KEYCODE_6, ctrl()),
                    keycode(KeyEvent.KEYCODE_NUMPAD_6, ctrl()),
                )
            TOGGLE_FLAG_PURPLE ->
                listOf(
                    keycode(KeyEvent.KEYCODE_7, ctrl()),
                    keycode(KeyEvent.KEYCODE_NUMPAD_7, ctrl()),
                )
            FLIP_OR_ANSWER_EASE1 ->
                listOf(
                    keycode(KeyEvent.KEYCODE_BUTTON_Y),
                    keycode(KeyEvent.KEYCODE_1, side = CardSide.ANSWER),
                    keycode(KeyEvent.KEYCODE_NUMPAD_1, side = CardSide.ANSWER),
                )
            FLIP_OR_ANSWER_EASE2 ->
                listOf(
                    keycode(KeyEvent.KEYCODE_BUTTON_X),
                    keycode(KeyEvent.KEYCODE_2, side = CardSide.ANSWER),
                    keycode(KeyEvent.KEYCODE_NUMPAD_2, side = CardSide.ANSWER),
                )
            FLIP_OR_ANSWER_EASE3 ->
                listOf(
                    keycode(KeyEvent.KEYCODE_BUTTON_B),
                    keycode(KeyEvent.KEYCODE_3, side = CardSide.ANSWER),
                    keycode(KeyEvent.KEYCODE_NUMPAD_3, side = CardSide.ANSWER),
                    keycode(KeyEvent.KEYCODE_DPAD_CENTER),
                    keycode(KeyEvent.KEYCODE_SPACE, side = CardSide.BOTH),
                    keycode(KeyEvent.KEYCODE_ENTER, side = CardSide.ANSWER),
                    keycode(KeyEvent.KEYCODE_NUMPAD_ENTER, side = CardSide.ANSWER),
                )
            FLIP_OR_ANSWER_EASE4 ->
                listOf(
                    keycode(KeyEvent.KEYCODE_BUTTON_A),
                    keycode(KeyEvent.KEYCODE_4, side = CardSide.ANSWER),
                    keycode(KeyEvent.KEYCODE_NUMPAD_4, side = CardSide.ANSWER),
                )
            // No default gestures
            SHOW_ANSWER,
            DELETE,
            CARD_INFO,
            TAG,
            EXIT,
            RESCHEDULE_NOTE,
            USER_ACTION_1,
            USER_ACTION_2,
            USER_ACTION_3,
            USER_ACTION_4,
            USER_ACTION_5,
            USER_ACTION_6,
            USER_ACTION_7,
            USER_ACTION_8,
            USER_ACTION_9,
            // Menu flag actions. They set the flag, but don't toggle it
            UNSET_FLAG,
            FLAG_RED,
            FLAG_ORANGE,
            FLAG_BLUE,
            FLAG_GREEN,
            FLAG_PINK,
            FLAG_TURQUOISE,
            FLAG_PURPLE,
            // Menu only
            DECK_OPTIONS,
            BURY_MENU,
            SUSPEND_MENU,
            FLAG_MENU,
            -> emptyList()
        }

    fun isSubMenu() = ViewerAction.entries.any { it.parentMenu == this }

    fun title(context: Context): String =
        when (this) {
            RESCHEDULE_NOTE -> TR.actionsSetDueDate().toSentenceCase(context, R.string.sentence_set_due_date)
            else -> context.getString(titleRes)
        }

    private fun keycode(
        keycode: Int,
        keys: ModifierKeys = ModifierKeys.none(),
        side: CardSide = CardSide.BOTH,
    ): ReviewerBinding {
        val binding = Binding.keyCode(keycode, keys)
        return ReviewerBinding(binding = binding, side = side)
    }

    private fun unicode(
        unicodeChar: Char,
        keys: ModifierKeys = AppDefinedModifierKeys.allowShift(),
        side: CardSide = CardSide.BOTH,
    ): ReviewerBinding {
        val binding = Binding.unicode(unicodeChar, keys)
        return ReviewerBinding(binding = binding, side = side)
    }

    companion object {
        fun fromId(
            @IdRes id: Int,
        ): ViewerAction = entries.first { it.menuId == id }

        fun fromPreferenceKey(preferenceKey: String): ViewerAction? = entries.firstOrNull { it.preferenceKey == preferenceKey }

        fun getSubMenus(): List<ViewerAction> = ViewerAction.entries.mapNotNull { it.parentMenu }.distinct()
    }
}
