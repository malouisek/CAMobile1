package com.app.marcellebike
//Student Name: Marcelle Louise de Souza
//Student ID: 19534

//For Map Markers
class Position (var lat: Double, var lng: Double )

//Station Objects
class BikeStation(var name: String, var number: String, var position: Position, var available_bikes: String)

//Created to allow more than one property be stored in a Marker Tag
//https://developers.google.com/android/reference/com/google/android/gms/maps/model/Marker
class MarkHelper( name: String, number: String, available_bikes: String) {

    var name: String = name
    get() {
        return field
    }
    var number: String = number
    get(){
        return field
    }

    var available_bikes: String = available_bikes
        get(){
            return field
        }

}
