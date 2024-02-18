package com.lvsmsmch.lchat.di

import androidx.room.Room
import com.lvsmsmch.lchat.data.db.AppDB
import com.lvsmsmch.lchat.data.db.DATABASE_NAME
import com.lvsmsmch.lchat.domain.repositories.CharactersRepository
import com.lvsmsmch.lchat.data.repository.MessagesRepositoryImpl
import com.lvsmsmch.lchat.data.repository.AiRepositoryOpenAiImpl
import com.lvsmsmch.lchat.data.repository.CharactersRepositoryImpl
import com.lvsmsmch.lchat.data.repository.SharedPrefsRepositoryImpl
import com.lvsmsmch.lchat.domain.repositories.AiRepository
import com.lvsmsmch.lchat.domain.repositories.MessagesRepository
import com.lvsmsmch.lchat.domain.repositories.SharedPrefsRepository
import com.lvsmsmch.lchat.presentation.pages.characters.CharactersViewModel
import com.lvsmsmch.lchat.presentation.pages.chat.ChatViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule
    get() = module {

        // base stuff
        single<SharedPrefsRepository> { SharedPrefsRepositoryImpl(androidContext()) }

        // DB
        single {
            Room.databaseBuilder(androidContext(), AppDB::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()   // todo remove before release
                .build()
        }
        single { get<AppDB>().messagesDao() }


        // Repositories
        single<CharactersRepository> { CharactersRepositoryImpl() }
        single<MessagesRepository> { MessagesRepositoryImpl(get()) }
        single<AiRepository> { AiRepositoryOpenAiImpl(get()) }   // OPEN AI STREAM


        // View Models
        viewModel { (characterId: Int) ->
            ChatViewModel(
                characterId = characterId,
                charactersRepository = get(),
                aiRepository = get(),
                messagesRepository = get(),
            )
        }

        viewModel {
            CharactersViewModel(
                charactersRepository = get(),
            )
        }
    }