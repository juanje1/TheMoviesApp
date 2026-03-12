package com.juanje.framework.database.paging

import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory

@AssistedFactory
interface MovieRemoteMediatorFactory {
    fun create(@Assisted("userName") userName: String, @Assisted("category") category: String): MovieRemoteMediator
}