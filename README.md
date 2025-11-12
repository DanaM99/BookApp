# 📚 BookApp

BookApp es una aplicación móvil desarrollada en **Android Studio (Kotlin + Jetpack Compose)** que permite a los usuarios **buscar, guardar y gestionar sus libros favoritos**, así como agregar comentarios, puntuaciones y controlar cuáles ya fueron leídos.  
La aplicación utiliza **Firebase Firestore** para el almacenamiento de datos y **Firebase Authentication** para la gestión de usuarios.

---

## 🚀 Funcionalidades principales

###  Autenticación de usuario
- Registro e inicio de sesión mediante **Firebase Authentication**.  
- Cada usuario tiene su propia colección de libros guardados.

###  Búsqueda de libros
- Búsqueda en tiempo real de libros utilizando una API (Google Books).  
- Muestra título, autor, cantidad de páginas e imagen de portada.  
- Permite seleccionar un libro para ver su descripción detallada.

###  Gestión de libros guardados
- Guardado de libros en la nube (Firestore) para cada usuario autenticado.  
- Visualización de todos los libros guardados en una lista con tarjetas visuales.  
- Eliminación de libros de la biblioteca personal.

###  Detalle del libro guardado
- Visualización completa del libro con su portada, autor, descripción y cantidad de páginas.  
- Posibilidad de:
  - Cambiar el **estado de lectura** (“Leído” o “No leído”).  
  - Asignar una **puntuación** con un slider de 0 a 5.  
  - Agregar un **comentario o reseña personal**.  
- Los cambios se guardan automáticamente en Firestore.

###  Filtro visual de lectura
- En la sección “Mis libros guardados” se pueden filtrar los libros por estado:
  -  **Todos**
  -  **Leídos**
- El filtro es **visual y dinámico**, sin recargar los datos desde Firebase.

---

## 🧠 Arquitectura y tecnologías

- **Lenguaje:** Kotlin  
- **Framework:** Jetpack Compose  
- **Base de datos:** Firebase Firestore  
- **Autenticación:** Firebase Auth  

---

## 💻 Integrantes


*[Ailín Luz Piffer](https://github.com/luzpiffer)* 

*[Dana Perez Moreno](https://github.com/DanaM99)* 

*[Micaela Navarro](https://github.com/micaelanavarrovdr)* 

### 1️⃣ Clonar el repositorio
```bash
git clone https://github.com/tuusuario/BookApp.git
