package com.graltion.aquaclean.utils

enum class CleaningMode {
    LIGHT,
    NORMAL,
    DEEP
}

enum class DeviceType {
    PHONE,
    HEADPHONES
}

fun CleaningMode.getFrequency(deviceType: DeviceType): Float {
    return when (deviceType) {
        DeviceType.PHONE -> when (this) {
            CleaningMode.LIGHT -> Constants.FREQ_LIGHT
            CleaningMode.NORMAL -> Constants.FREQ_NORMAL
            CleaningMode.DEEP -> Constants.FREQ_DEEP
        }
        DeviceType.HEADPHONES -> when (this) {
            CleaningMode.LIGHT -> Constants.FREQ_LIGHT_HEADPHONES
            CleaningMode.NORMAL -> Constants.FREQ_NORMAL_HEADPHONES
            CleaningMode.DEEP -> Constants.FREQ_DEEP_HEADPHONES
        }
    }
}

fun CleaningMode.getVolume(deviceType: DeviceType): Float {
    return when (deviceType) {
        DeviceType.PHONE -> when (this) {
            CleaningMode.LIGHT -> Constants.VOLUME_LIGHT
            CleaningMode.NORMAL -> Constants.VOLUME_NORMAL
            CleaningMode.DEEP -> Constants.VOLUME_DEEP
        }
        DeviceType.HEADPHONES -> when (this) {
            CleaningMode.LIGHT -> Constants.VOLUME_LIGHT_HEADPHONES
            CleaningMode.NORMAL -> Constants.VOLUME_NORMAL_HEADPHONES
            CleaningMode.DEEP -> Constants.VOLUME_DEEP_HEADPHONES
        }
    }
}

fun CleaningMode.getDurationRange(): IntRange {
    return when (this) {
        CleaningMode.LIGHT -> Constants.DURATION_LIGHT_MIN..Constants.DURATION_LIGHT_MAX
        CleaningMode.NORMAL -> Constants.DURATION_NORMAL_MIN..Constants.DURATION_NORMAL_MAX
        CleaningMode.DEEP -> Constants.DURATION_DEEP_MIN..Constants.DURATION_DEEP_MAX
    }
}

fun CleaningMode.getDefaultDuration(): Int {
    return when (this) {
        CleaningMode.LIGHT -> Constants.DURATION_LIGHT_DEFAULT
        CleaningMode.NORMAL -> Constants.DURATION_NORMAL_DEFAULT
        CleaningMode.DEEP -> Constants.DURATION_DEEP_DEFAULT
    }
}
