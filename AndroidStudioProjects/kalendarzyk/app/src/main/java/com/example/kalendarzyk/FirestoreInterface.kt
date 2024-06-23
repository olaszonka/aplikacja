package com.example.kalendarzyk

import com.example.kalendarzyk.firestore.User

/**
 * Interfejs FirestoreInterface definiuje operacje do interakcji z bazą danych Firestore.
 * Wszystkie operacje są oznaczone jako 'suspend', co oznacza, że mogą być wywoływane
 * tylko w kontekście korutyny.
 */
interface FirestoreInterface {

    /**
     * Suspend function do dodawania nowego rekordu (studenta) do bazy danych Firestore.
     *
     * @param userId Identyfikator nowego studenta.
     * @param user Obiekt klasy Student, który ma zostać dodany do bazy danych.
     */
    suspend fun addUser(userId: String, user: User)

    /**
     * Suspend function do pobierania danych studenta z bazy danych na podstawie jego ID.
     *
     * @param userId Identyfikator studenta, którego dane mają zostać pobrane.
     * @return Obiekt klasy Student odpowiadający danym studenta z bazy danych,
     * lub null, jeśli student o podanym identyfikatorze nie istnieje.
     */
    suspend fun getUser(userId: String): User?

    /**
     * Suspend function do aktualizacji istniejącego rekordu (studenta) w bazie danych Firestore.
     *
     * @param userId Identyfikator studenta, którego dane mają zostać zaktualizowane.
     * @param updatedUser Obiekt klasy Student z zaktualizowanymi danymi.
     */
    suspend fun updateUser(userId: String, updatedUser: User)

    /**
     * Suspend function do usuwania istniejącego rekordu (studenta) z bazy danych na podstawie jego ID.
     *
     * @param userId Identyfikator studenta, którego dane mają zostać usunięte.
     */
    suspend fun deleteUser(userId: String)
}