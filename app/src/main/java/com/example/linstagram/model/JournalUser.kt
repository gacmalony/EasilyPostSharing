package com.example.linstagram.model

class JournalUser {

    var username:String? = null
    var userId:String? = null

    companion object{
        var instanceUser: JournalUser? = null
            get(){
                if(field==null){
                    field = JournalUser()
                }
                return field
            }
            private set
    }



}