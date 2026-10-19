# 📱 App3Apis - Trabajo Práctico de Programación 3

Aplicación móvil nativa para Android desarrollada en **Jetpack Compose** bajo la arquitectura **MVVM (Model-View-ViewModel)**. 

La aplicación consume de forma independiente **3 APIs públicas, 100% gratuitas, sin registros y sin claves**, mostrando un dashboard interactivo de clima y museos de arte mundial con carruseles horizontales y pantallas de detalle completas.

---

## 🏛️ Las 3 APIs Integradas

1. **Open-Meteo API (Clima y Meteorología):**
   - *Documentación:* [https://open-meteo.com/en/docs](https://open-meteo.com/en/docs)
   - *Uso:* Clima actual, temperatura en °C y velocidad del viento.
2. **Art Institute of Chicago API (Catálogo de Arte):**
   - *Documentación:* [https://api.artic.edu/docs/#quick-start](https://api.artic.edu/docs/#quick-start)
   - *Uso:* Obras maestras y colecciones de arte del instituto de Chicago con imágenes IIIF.
3. **The Metropolitan Museum of Art API (Colección del MET):**
   - *Documentación:* [https://metmuseum.github.io/](https://metmuseum.github.io/)
   - *Uso:* Acceso abierto a artefactos y pinturas clásicas del museo MET de Nueva York.

---

## 📂 Cumplimiento de Requisitos

La estructura del proyecto está rigurosamente organizada por capas siguiendo los principios de arquitectura limpia y MVVM:

### 1. 🌐 3 Servicios de Red Independientes (Retrofit)
Ubicados en `com/example/app3apis/data/remote/`:
- **[OpenMeteoApiService.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/remote/OpenMeteoApiService.kt)** -> Servicio HTTP para el clima.
- **[ArticApiService.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/remote/ArticApiService.kt)** -> Servicio HTTP para el Art Institute of Chicago.
- **[MetApiService.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/remote/MetApiService.kt)** -> Servicio HTTP para el MET Museum.

### 2. 📦 3 DTOs (Data Transfer Objects / Clases de Respuesta JSON)
Ubicados en `com/example/app3apis/data/model/`:
- **[WeatherResponseDto.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/model/WeatherResponseDto.kt)** -> DTOs de respuesta meteorológica.
- **[ArticResponseDto.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/model/ArticResponseDto.kt)** -> DTOs de obras de Chicago.
- **[MetResponseDto.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/model/MetResponseDto.kt)** -> DTOs de búsqueda y detalle de objetos del MET.

### 3. 🔄 3 Mappers (Transformación DTO ➡️ Modelos de Dominio)
Ubicados en `com/example/app3apis/data/mapper/`:
- **[WeatherMapper.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/mapper/WeatherMapper.kt)** -> Transforma `WeatherResponseDto` a `WeatherModel`.
- **[ArticMapper.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/mapper/ArticMapper.kt)** -> Transforma `ArticArtworkDto` a `ArtworkModel`.
- **[MetMapper.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/mapper/MetMapper.kt)** -> Transforma `MetObjectDto` a `ArtworkModel`.

### 4. 🗄️ 3 Repositorios
Ubicados en `com/example/app3apis/data/repository/`:
- **[WeatherRepository.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/repository/WeatherRepository.kt)** -> Gestiona la fuente de datos del clima.
- **[ArticRepository.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/repository/ArticRepository.kt)** -> Gestiona la fuente de datos de Chicago.
- **[MetRepository.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/data/repository/MetRepository.kt)** -> Gestiona la fuente de datos del MET.

### 5. ⚙️ ViewModels y Vistas (Screens) en Jetpack Compose (MVVM)
- **ViewModel:** **[DashboardViewModel.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/ui/viewmodel/DashboardViewModel.kt)** -> Ejecuta peticiones concurrentes con corrutinas (`viewModelScope`) y expone el estado con `StateFlow`.
- **Vistas (UI):**
  - **[HomeScreen.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/ui/screens/HomeScreen.kt)** -> Dashboard principal con tarjeta de clima, obra destacada y carruseles horizontales.
  - **[DetailScreen.kt](file:///C:/Users/adry4/AndroidStudioProjects/App3Apis/app/src/main/java/com/example/app3apis/ui/screens/DetailScreen.kt)** -> Pantalla de detalle completa al hacer clic en cualquier obra.

---

## 🚀 Cómo Ejecutar la Aplicación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/adrianabolivarm/app3Apis.git
   ```
2. Abre el proyecto en **Android Studio** (versión Jellyfish o superior recomendada).
3. Asegúrate de tener conexión a Internet (permiso configurado en el `AndroidManifest.xml`).
4. Ejecuta la app en un emulador Android (API 24+) o en un dispositivo físico con Depuración USB / Inalámbrica.
