package com.example.edupulse.data.model

data class Users(
    val id:String?=null,
    var fullName:String?=null,
    val email:String?=null,
    val password:String?=null,
    val image:String?=null,
    val message:String?=null,
)

data class UserAnswersQuestionnaire(
    val question:String?=null,
    val userAnswer:String?=null
)



