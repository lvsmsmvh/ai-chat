package com.lvsmsmch.lchat.presentation.theme

import androidx.compose.ui.graphics.Color

object Colors {
    val Purple80 = Color(0xFFD0BCFF)
    val PurpleGrey80 = Color(0xFFCCC2DC)
    val Pink80 = Color(0xFFEFB8C8)

    val Purple40 = Color(0xFF6650a4)
    val PurpleGrey40 = Color(0xFF625b71)
    val Pink40 = Color(0xFF7D5260)

    val Transparent = Color(0x00000000)



    object Chat {
        val Bg = Color(0xFF222222)
        val BgForSelectedMessage = Color(0x77114477)
        val BgSelectingBar = Color(0xFF333333)

        val SenderUserText = Color(0xFFFFFFFF)
        val SenderCharacterText = Color(0xFF66BBFF)

        val MessageErrorText = Color(0xFFCC1111)
        val MessageUserText = Color(0xFFFFFFFF)
        val MessageCharacterText = Color(0xFFFFFFFF)

        val MessageUserBg = Color(0xFF333333)
        val MessageUserBgSelected = Color(0xFF335577)
        val MessageCharacterBg = Color(0xFF114477)
        val MessageCharacterBgFailed = Color(0xFF993322)
        val MessageCharacterBgSelected = Color(0xFF226699)
        val MessageCharacterBgFailedSelected = Color(0xFF773322)


        val InputBoxSeparator = Color(0xFF555555)
        val InputBoxBg = Color(0xFF333333)
        val InputBoxText = Color(0xFFFFFFFF)
        val InputBoxHint = Color(0x44FFFFFF)

        val BtnSendBg = Color(0xFF114477)
        val BtnSendBgDisabled = Color(0xFF333333)
        val BtnSendText = Color(0xFFFFFFFF)
    }

    object Characters {
        val Bg = Color(0xFF222222)
        val ButtonBg = Color(0xFF114477)
        val ButtonText = Color(0xFFFFFFFF)
        val CardBg = Color(0xFF333333)
        val CardText = Color(0xFFFFFFFF)
    }
}