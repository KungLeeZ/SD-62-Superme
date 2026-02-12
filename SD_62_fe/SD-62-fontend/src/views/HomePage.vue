<!-- src/views/HomePage.vue -->
<template>
  <div class="home">
    <!-- Hero Banner -->
    <div class="bg-dark text-white p-5 mb-4 rounded-3">
      <div class="container py-5">
        <h1 class="display-4 fw-bold">
          <span class="text-danger">SUPREME</span> SNEAKER
        </h1>
        <p class="col-md-8 fs-4 text-white-50">
          Giày sneaker chính hãng - New drop mỗi thứ 5
        </p>
        <router-link to="/products" class="btn btn-danger btn-lg">
          <i class="bi bi-bag"></i> Mua ngay
        </router-link>
      </div>
    </div>

    <!-- Features -->
    <div class="row g-4 py-4">
      <div class="col-md-3 col-6 text-center">
        <i class="bi bi-truck fs-1 text-danger"></i>
        <h6 class="mt-2">Free Ship</h6>
        <small class="text-muted">Từ 1.5tr</small>
      </div>
      <div class="col-md-3 col-6 text-center">
        <i class="bi bi-shield-check fs-1 text-danger"></i>
        <h6 class="mt-2">Chính hãng</h6>
        <small class="text-muted">100% Authentic</small>
      </div>
      <div class="col-md-3 col-6 text-center">
        <i class="bi bi-arrow-return-left fs-1 text-danger"></i>
        <h6 class="mt-2">Đổi trả</h6>
        <small class="text-muted">30 ngày</small>
      </div>
      <div class="col-md-3 col-6 text-center">
        <i class="bi bi-gift fs-1 text-danger"></i>
        <h6 class="mt-2">Quà tặng</h6>
        <small class="text-muted">Tặng sock</small>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HomePage'
}
</script>