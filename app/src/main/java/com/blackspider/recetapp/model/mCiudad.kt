package com.blackspider.recetapp.model

data class mCiudad (val ciudadid : Long, val ciudad : String){

    override fun toString () : String {

        return this.ciudad

    }

}