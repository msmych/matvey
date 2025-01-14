package uk.matvey.app.director

import com.github.jasync.sql.db.Connection
import com.github.jasync.sql.db.RowData
import uk.matvey.kit.time.TimeKit.instant

object DirectorSql {

    fun Connection.getDirectors(ids: List<Int>): List<Director> {
        return sendPreparedStatement(
            """
                    SELECT * 
                    FROM directors 
                    WHERE id in (${ids.joinToString()})
                    """.trimIndent(),
        )
            .join()
            .rows
            .map { it.toDirector() }
    }

    fun Connection.addDirector(id: Int, name: String) {
        sendPreparedStatement(
            """
                INSERT INTO directors (id, name, created_at) 
                VALUES (?, ?, ?)
                ON CONFLICT (id) DO NOTHING
                """.trimIndent(),
            listOf(id, name, instant().toString())
        )
            .join()
    }

    fun RowData.toDirector(): Director {
        return Director(
            id = requireNotNull(getInt("id")),
            name = requireNotNull(getString("name")),
        )
    }
}