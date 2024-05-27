package com.example.nasizae_edu_pulse.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.nasizae_edu_pulse.data.model.PersonalLibraryModel
@Dao
interface LibraryDao {

    @Query("SELECT * FROM PersonalLibraryModel ORDER BY id DESC")
    fun getAll():List<PersonalLibraryModel>
    @Query("SELECT * FROM PersonalLibraryModel WHERE id=:taskId")
    fun getById(taskId: Int): List<PersonalLibraryModel>

    @Insert
    fun insert(library: PersonalLibraryModel)

}
