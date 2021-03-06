package ru.merkulyevsasha.dbrepository.database.mappers

import ru.merkulyevsasha.core.mappers.Mapper
import ru.merkulyevsasha.core.models.RssSource
import ru.merkulyevsasha.database.entities.RssSourceEntity

class RssSourceMapper : Mapper<RssSource, RssSourceEntity> {
    override fun map(item: RssSource): RssSourceEntity {
        return RssSourceEntity(item.sourceId, item.sourceName, item.checked)
    }
}