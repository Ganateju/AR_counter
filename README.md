# AR Counter (Early Stage) 🎯

This is an Android app I’m building to help count physical objects (like bricks, boxes, or stock) in real-time using the camera. 

The idea for this project actually came from my father. He asked me if I could create something to help him count items more efficiently at his office. This gave me a real-world problem to solve while I'm on my journey of learning Android development.

## 🚀 How it Works
The app uses a "point-and-scan" method. As you move your camera over objects, the AI detects them and assigns a coordinate. To prevent double-counting the same item twice, I added a spatial logic that ignores detections that are too close to a spot that was already counted.

### The "Sensitivity Slider"
While testing for my father's needs, I realized that different objects vary in size and spacing. I added a manual slider that lets the user adjust the "counting distance" on the fly:
* **Lower Sensitivity:** Better for small, tightly packed items (like bricks or small parts).
* **Higher Sensitivity:** Better for large, spread-out items (like shipping pallets or office furniture).

## 🛠 What I Used
* **Kotlin** - The core language.
* **OpenCV Android SDK** - For high-speed image processing frames.
* **Google ML Kit** - For the object detection AI.
* **XML / AppCompat** - For the UI (I chose this for better stability with the camera view).

## 🚧 My Learning Progress (Known Issues)
Since I am still learning, there are a few things I'm still working on:
* **Lighting:** The AI struggles in dark environments (I'm looking into adding a flashlight toggle next).
* **Speed:** If you move the camera too fast, the AI might miss a frame.
* **Code Quality:** I am still learning how to organize my code properly, so the `MainActivity` is currently handling a lot of the work.

## 📥 Testing the App
I have uploaded a **Debug APK** in the "Releases" section. Since this is a pre-release and I haven't signed it with a developer key yet, Android will show a "Play Protect" warning. You'll need to click "Install Anyway" to try it out.

---
*I'm building this to be a helpful tool for my dad, but I'm also open to suggestions or tips from more experienced developers on how to improve the tracking!*
