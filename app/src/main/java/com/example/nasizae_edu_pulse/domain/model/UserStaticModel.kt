package com.example.nasizae_edu_pulse.domain.model

data class UserStaticModel(
    val health:Int?=null,
)
data class UserDataStaticTasks(
    val nameThemeWork:String?=null,
    val countUserLvl:Int?=null,
)
data class UserDataStaticResult(
    val progressNumber:Int=0,
    val maxCountProgress:Int=0,

)
data class userResult(
    val time:String?=null,
    val total:Int?=null,
    val tasksNumber:Int?=null
)

