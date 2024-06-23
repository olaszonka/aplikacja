package com.example.kalendarzyk.firestore

import com.example.kalendarzyk.Data
import java.util.Calendar

class User(val email: String="",
            val userId: String="",
            val name: String="",
            val registeredUser: Boolean = false,
            val first: Calendar?
)