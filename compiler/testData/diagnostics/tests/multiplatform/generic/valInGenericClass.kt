// !LANGUAGE: +MultiPlatformProjects
// MODULE: m1-common
// FILE: common.kt

header interface A<T> {
    val x: T
}

// MODULE: m2-jvm(m1-common)
// FILE: jvm.kt

impl interface A<T> {
    impl val x: T
}
