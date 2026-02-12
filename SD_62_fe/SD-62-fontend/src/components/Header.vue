<template>
  <header>
    <nav
        class="navbar navbar-expand-lg navbar-light bg-light border-bottom shadow-sm fixed-top"
        style="height: 80px;"
    >
      <div class="container-fluid d-flex align-items-center justify-content-between">
        <!-- 🟢 Logo -->
        <router-link class="nav-link active" to="/">
          <img
              src="@/assets/logo_ngang.jpg"
              alt="Logo"
              width="150"
              class="d-inline-block align-text-top me-2"
          />
        </router-link>

        <!-- 🟡 Menu bên trái -->
        <ul class="navbar-nav flex-row">
          <li class="nav-item me-3">
            <router-link class="nav-link active" to="/">Trang chủ</router-link>
          </li>

          <!-- 🧩 Danh mục -->
          <li
              v-if="user && (user.roleName === 'ADMIN' || user.roleName === 'STAFF')"
              class="nav-item dropdown me-3"
          >
            <a
                class="nav-link dropdown-toggle"
                href="#"
                role="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
            >
              Danh mục
            </a>
            <ul class="dropdown-menu dropdown-menu-end">
              <li v-if="user.roleName === 'ADMIN'">
                <router-link class="dropdown-item" to="/users">Tài khoản</router-link>
              </li>
              <li><router-link class="dropdown-item" to="/authors">Tác giả</router-link></li>
              <li><router-link class="dropdown-item" to="/books">Sách</router-link></li>
              <li><router-link class="dropdown-item" to="/book-variants">Phiên bản sách</router-link></li>
              <li><router-link class="dropdown-item" to="/book-copy">Bản ghi sách</router-link></li>
              <li><router-link class="dropdown-item" to="/publishers">Nhà xuất bản</router-link></li>
              <li><router-link class="dropdown-item" to="/documents">Loại tài liệu</router-link></li>
              <li><router-link class="dropdown-item" to="/categories">Thể loại</router-link></li>
              <li><router-link class="dropdown-item" to="/languages">Ngôn ngữ</router-link></li>
              <li><router-link class="dropdown-item" to="/bills">Hóa đơn nhập sách</router-link></li>
              <li><router-link class="dropdown-item" to="/announcements">Thông báo</router-link></li>
            </ul>
          </li>

          <!-- 🧠 Reader -->
          <li v-if="!user || user.roleName === 'READER'" class="nav-item me-3">
            <router-link class="nav-link" to="/booksReader">Sách Reader</router-link>
          </li>

          <li class="nav-item me-3">
            <a class="nav-link" href="#">Liên hệ</a>
          </li>

          <li class="nav-item">
            <a class="nav-link disabled" aria-disabled="true">Đang phát triển</a>
          </li>
        </ul>

        <!-- 🟢 Khu vực user -->
        <div class="d-flex align-items-center">
          <!-- Hiển thị thông tin user -->
          <div v-if="user" class="me-3 text-end">
            <span class="fw-semibold">👋 Xin chào, {{ user.name }}</span>
            <span class="text-muted small d-block">({{ user.roleName }})</span>
          </div>

          <!-- Đăng xuất / đăng nhập -->
          <button
              v-if="user"
              class="btn btn-outline-danger btn-sm"
              @click="logout"
          >
            Đăng xuất
          </button>

          <router-link
              v-else
              class="btn btn-primary btn-sm"
              to="/LoginRegister"
          >
            Đăng nhập
          </router-link>
        </div>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'   // 🟢 thêm watch ở đây
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const user = ref(null)

// 🟢 Lấy user từ localStorage khi load trang hoặc sau khi login
function loadUser() {
  const savedUser = localStorage.getItem('user')
  user.value = savedUser ? JSON.parse(savedUser) : null
}

// 🛑 Logout
function logout() {
  localStorage.removeItem('user')
  user.value = null
  router.push('/LoginRegister')
}

// 🔄 Mỗi khi route thay đổi, thử load lại user
onMounted(loadUser)
watch(() => route.fullPath, loadUser)
</script>



<style scoped>
.navbar-nav .nav-link {
  font-weight: 500;
}

.navbar {
  z-index: 9999;
}
</style>
