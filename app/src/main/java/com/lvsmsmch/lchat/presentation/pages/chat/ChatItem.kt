package com.lvsmsmch.lchat.presentation.pages.chat

sealed class ChatItem(
    open val id: Int,
    open val isSelected: Boolean,
) {

    abstract fun copyWithDifferentSelect(
        isSelected: Boolean = this.isSelected
    ): ChatItem

    data class UserMessage(
        override val id: Int,
        override val isSelected: Boolean = false,
        val messageText: String,
        val sendTimeMillis: Long,
    ) : ChatItem(
        id = id,
        isSelected = false
    ) {
        override fun copyWithDifferentSelect(isSelected: Boolean) = copy(
            isSelected = isSelected
        )
    }

    data class CharacterMessage(
        override val id: Int,
        override val isSelected: Boolean = false,
        val messageText: String,
        val sendTimeMillis: Long,
    ) : ChatItem(
        id = id,
        isSelected = false
    ) {
        override fun copyWithDifferentSelect(isSelected: Boolean) = copy(
            isSelected = isSelected
        )
    }

    data class CharacterFailed(
        override val id: Int,
        override val isSelected: Boolean = false,
        val sendTimeMillis: Long,
    ) : ChatItem(
        id = id,
        isSelected = false
    ) {
        override fun copyWithDifferentSelect(isSelected: Boolean) = copy(
            isSelected = isSelected
        )
    }


    data class CharacterTyping(
        val messageText: String,
    ) : ChatItem(
        id = NEXT_MESSAGE_ID,
        isSelected = false
    ) {
        override fun copyWithDifferentSelect(isSelected: Boolean) = throw NotImplementedError()
    }

    data object CharacterLoading : ChatItem(
        id = NEXT_MESSAGE_ID,
        isSelected = false
    ) {
        override fun copyWithDifferentSelect(isSelected: Boolean) = throw NotImplementedError()
    }

    companion object {

        /**
         *  Used for a typing or loading message, that is not in database yet.
         */
        const val NEXT_MESSAGE_ID = Int.MAX_VALUE

    }
}