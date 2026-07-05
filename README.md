# 🎯 AR Counter

### Spatial Intelligence Platform | Persistent Object Counting & Mobile Vision

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)]()
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white)]()
[![OpenCV](https://img.shields.io/badge/CV-OpenCV-5C3EE8?logo=opencv&logoColor=white)]()
[![ML Kit](https://img.shields.io/badge/AI-ML_Kit-4285F4?logo=google&logoColor=white)]()
[![License](https://img.shields.io/badge/License-MIT-00FF41.svg)]()

---

## 📌 Overview

AR Counter is a mobile computer vision platform designed for real-time object counting in physical environments.

The project originated from a practical inventory problem encountered in office workflows, where repetitive manual counting introduces inefficiencies and human error.

Rather than simply detecting objects, AR Counter focuses on **persistent counting**, ensuring that previously counted objects are not repeatedly registered as the camera moves.

---

# Status

| Metric | Value |
|--------|-------|
| Current State | Functional MVP |
| Platform | Android |
| Deployment | Debug APK Released |
| Validation | Real-world Inventory Testing |
| Version | Early Stage |

---

## Current Focus

- Persistent Object Tracking
- Counting Stability
- Low-Light Performance
- Spatial Anchoring
- User Experience Optimization

---

# 🌟 Core Innovation

AR Counter derives counting reliability through three complementary mechanisms.

| Component | Objective |
|-----------|-----------|
| Object Detection | Identify physical entities in real time |
| Spatial Anchoring | Assign persistent coordinates to detected objects |
| Adaptive Sensitivity | Dynamically adjust duplicate suppression thresholds |

Together these mechanisms create a lightweight inventory intelligence system capable of operating entirely on mobile hardware.

---

# 📡 Processing Pipeline

| Stage | Purpose |
|-------|---------|
| Camera Acquisition | Capture live video stream |
| Object Detection | ML Kit inference |
| Coordinate Assignment | Spatial anchoring |
| Duplicate Suppression | Persistent ID validation |
| Sensitivity Adjustment | Dynamic threshold tuning |
| Counting Engine | Increment object count |
| Visualization | Overlay detections |
| Reporting | Inventory statistics |

---

# 🛠️ Technology Stack

| Domain | Stack |
|--------|-------|
| Mobile Development | Kotlin |
| Computer Vision | OpenCV |
| AI Inference | Google ML Kit |
| User Interface | XML, AppCompat |
| Tracking Logic | Spatial Anchoring |
| Deployment | Android APK |

---

# 🎚 Adaptive Sensitivity System

Different objects exhibit varying densities and spacing patterns.

AR Counter introduces an adjustable sensitivity mechanism allowing users to calibrate the counting radius according to the operating environment.

### Lower Sensitivity
Optimized for:

- Bricks
- Small Parts
- Dense Inventory
- Packed Storage

### Higher Sensitivity

Optimized for:

- Boxes
- Office Equipment
- Pallets
- Large Objects

---

# ⚠ Known Challenges

- Low-light degradation
- Frame skipping during rapid motion
- Occlusion handling
- Large object overlap
- Code modularization

---

# 📥 Testing

A debug APK is available in the **Releases** section.

As this is an unsigned debug build, Android may display a Play Protect warning.

Select:

```text
Install Anyway
```

to evaluate the application.

---

# Engineering Domains

- Computer Vision
- Mobile Development
- Spatial Intelligence
- Edge AI
- Human-Centered Design
- Inventory Automation
- Systems Engineering

---

# 🧩 Developer Note

> The objective was never merely counting objects.

> The objective was maintaining identity through space.

AR Counter explores how lightweight computer vision systems can preserve object persistence under motion and environmental variability.

---

Built by Ganateju

*"Spatial awareness under motion."*
