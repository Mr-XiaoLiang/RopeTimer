package com.lollipop.ropetimer.panel.normal

enum class NormalPanelState {
    MINI,
    FULL,
    SETTING;

    val isMini: Boolean
        get() {
            return this == MINI
        }

    val isSetting: Boolean
        get() {
            return this == SETTING
        }
}