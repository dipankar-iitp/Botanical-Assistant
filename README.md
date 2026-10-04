# 🌿 Botanical Assistant (HerbalPedia)

An AI-powered Android application that serves as a multimodal botanical database assistant. Built with Kotlin and Jetpack Compose, this app leverages the Google Gemini API to analyze images of flora, medicinal plants, and herbs, delivering highly accurate, structured, and educational information directly to the user.

## ✨ Features

*   📸 **AI Plant Identification:** Capture or upload a photo to instantly identify plants, herbs, and medicinal flora.
*   🔬 **Detailed Visual Analysis:** Provides structured breakdowns of observed features (leaves, stems, flowers, environment).
*   📚 **Historical & Cultural Context:** Learn about the native regions and traditional significance of various species.
*   ⚕️ **Medicinal Insights:** Documents traditional therapeutic uses and highlights active chemical compounds.
*   ⚠️ **Toxicity & Safety Alerts (Critical):** Automatically flags toxic plants, dangerous look-alikes, and contraindications.
*   💾 **Digital Herbarium:** Saves scanned specimens locally using Room Database for offline reference and field guides.
*   📤 **Structured JSON Export:** Automatically parses AI responses into a clean JSON schema (`scientific_name`, `common_names`, `confidence_score`, `toxicity_warning`) for seamless database integration.

## 🛠️ Tech Stack

*   **Language:** Kotlin
*   **UI Toolkit:** Android Jetpack Compose
*   **Local Storage:** Room Database
*   **AI Integration:** Google Gemini API (Multimodal)
*   **Architecture:** MVVM (Model-View-ViewModel)

## 🚀 Getting Started

### Prerequisites
*   Android Studio (Latest version recommended)
*   A Google Gemini API Key

### Installation

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/yourusername/botanical-assistant.git](https://github.com/yourusername/botanical-assistant.git)
    cd botanical-assistant
    ```

2.  **Set up your API Key:**
    *   Rename the `.env.example` file to `.env` (or configure via `local.properties`).
    *   Add your Gemini API key:
        ```properties
        GEMINI_API_KEY=your_api_key_here
        ```

3.  **Build and Run:**
    *   Open the project in Android Studio.
    *   Sync the Gradle files.
    *   Run the app on a physical device or emulator (Android 8.0+ recommended).

## 📂 Project Structure

*   `app/src/main/java/com/example/ui/`: Contains all Jetpack Compose screens (`ScanScreen`, `HerbariumScreen`, `DetailScreen`, `ReferenceGuideScreen`) and UI components.
*   `app/src/main/java/com/example/data/`: Handles data models, API integration (`GeminiBotanicalService`), and local persistence (`Room` database DAOs and Repositories).
*   `app/src/main/java/com/example/ui/viewmodel/`: Manages state and business logic.

## 🧪 Testing

The project includes a comprehensive suite of unit and Robolectric tests validating:
*   Botanical markdown parsing
*   JSON data extraction
*   Toxicity hazard detection (e.g., *Digitalis purpurea*, *Atropa belladonna*)
*   Database migrations

To run the tests:
```bash
./gradlew testDebugUnitTest
