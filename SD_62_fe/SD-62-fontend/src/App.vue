<template>
  <div div id="app" class="d-flex flex-column min-vh-100">
    <!-- Header -->
    <Header />

    <!-- Nội dung chính (thêm padding-top để không bị che) -->
    <main class="flex-grow-1 p-4 pt-5 mb-lg-5">
      <router-view />
    </main>

    <!-- Footer -->
    <Footer />
  </div>
</template>
<script setup>
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
</script>

