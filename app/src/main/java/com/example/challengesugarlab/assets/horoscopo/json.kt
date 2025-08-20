package com.example.challengesugarlab.assets.horoscopo

import com.example.challengesugarlab.R

data class Signo(
    val nombre: String,
    val imagen: Int,
    val caracteristicas: List<String>,
    val elemento: String,
    val compatibilidad: List<String>,
    val descripcion: String
)

val signosChinos = listOf(
    Signo(
        nombre = "Rata",
        imagen = R.drawable.rata,
        caracteristicas = listOf("Inteligente", "Versátil", "Sociable", "Astuta"),
        elemento = "Agua",
        compatibilidad = listOf("Dragón", "Mono"),
        descripcion = "Las personas nacidas bajo el signo de la Rata son astutas, inteligentes y sociables. Se adaptan rápidamente a cualquier situación y tienen gran capacidad de análisis."
    ),
    Signo(
        nombre = "Buey",
        imagen = R.drawable.buey,
        caracteristicas = listOf("Trabajador", "Fiel", "Paciente", "Persistente"),
        elemento = "Tierra",
        compatibilidad = listOf("Serpiente", "Gallo"),
        descripcion = "Los Bueyes son personas confiables y determinadas. Se destacan por su paciencia, sentido de responsabilidad y enfoque práctico."
    ),
    Signo(
        nombre = "Tigre",
        imagen = R.drawable.tigre,
        caracteristicas = listOf("Valiente", "Impulsivo", "Independiente", "Carismático"),
        elemento = "Madera",
        compatibilidad = listOf("Caballo", "Perro"),
        descripcion = "Los Tigres son valientes y llenos de energía. Les gusta la independencia y tienden a ser líderes naturales, con carácter apasionado y protector."
    ),
    Signo(
        nombre = "Conejo",
        imagen = R.drawable.conejo,
        caracteristicas = listOf("Sociable", "Elegante", "Pacífico", "Sensato"),
        elemento = "Madera",
        compatibilidad = listOf("Cabra", "Cerdo"),
        descripcion = "Los Conejos son personas diplomáticas, amables y elegantes. Prefieren la tranquilidad y suelen evitar los conflictos, buscando siempre armonía."
    ),
    Signo(
        nombre = "Dragón",
        imagen = R.drawable.dragon,
        caracteristicas = listOf("Valiente", "Carismático", "Creativo", "Apasionado"),
        elemento = "Tierra",
        compatibilidad = listOf("Rata", "Mono"),
        descripcion = "Los Dragones son poderosos y llenos de energía. Destacan por su creatividad y liderazgo, y siempre buscan alcanzar grandes metas."
    ),
    Signo(
        nombre = "Serpiente",
        imagen = R.drawable.serpiente,
        caracteristicas = listOf("Sabio", "Intuitivo", "Elegante", "Reservado"),
        elemento = "Fuego",
        compatibilidad = listOf("Buey", "Gallo"),
        descripcion = "Las Serpientes son inteligentes y perspicaces. Tienen gran intuición y prefieren pensar antes de actuar, siendo cautelosos en sus decisiones."
    ),
    Signo(
        nombre = "Caballo",
        imagen = R.drawable.caballo,
        caracteristicas = listOf("Activo", "Libre", "Optimista", "Enérgico"),
        elemento = "Fuego",
        compatibilidad = listOf("Tigre", "Cabra"),
        descripcion = "Los Caballos son personas enérgicas y amantes de la libertad. Su optimismo y sociabilidad les permite conectar fácilmente con otros."
    ),
    Signo(
        nombre = "Cabra",
        imagen = R.drawable.cabra,
        caracteristicas = listOf("Creativa", "Compasiva", "Paciente", "Amable"),
        elemento = "Tierra",
        compatibilidad = listOf("Conejo", "Caballo"),
        descripcion = "Las Cabras son personas sensibles y artísticas. Buscan la armonía y disfrutan ayudando a los demás, mostrando gran empatía."
    ),
    Signo(
        nombre = "Mono",
        imagen = R.drawable.mono,
        caracteristicas = listOf("Ingenioso", "Divertido", "Curioso", "Flexible"),
        elemento = "Metal",
        compatibilidad = listOf("Rata", "Dragón"),
        descripcion = "Los Monos son inteligentes y curiosos. Tienen gran habilidad para resolver problemas y suelen ser muy sociables y divertidos."
    ),
    Signo(
        nombre = "Gallo",
        imagen = R.drawable.gallo,
        caracteristicas = listOf("Valiente", "Honesto", "Organizado", "Directo"),
        elemento = "Metal",
        compatibilidad = listOf("Buey", "Serpiente"),
        descripcion = "Los Gallos son personas confiables y directas. Se destacan por su honestidad y disciplina, y suelen planear todo con detalle."
    ),
    Signo(
        nombre = "Perro",
        imagen = R.drawable.perro,
        caracteristicas = listOf("Leal", "Honesto", "Compasivo", "Responsable"),
        elemento = "Tierra",
        compatibilidad = listOf("Tigre", "Conejo"),
        descripcion = "Los Perros son leales y protectores. Valoran la justicia y la honestidad, y son excelentes amigos y confidentes."
    ),
    Signo(
        nombre = "Cerdo",
        imagen = R.drawable.cerdo,
        caracteristicas = listOf("Generoso", "Amable", "Pacífico", "Honesto"),
        elemento = "Agua",
        compatibilidad = listOf("Conejo", "Cabra"),
        descripcion = "Los Cerdos son personas generosas y confiables. Les gusta disfrutar de la vida y tienden a ser muy amables y comprensivos con los demás."
    )
)
