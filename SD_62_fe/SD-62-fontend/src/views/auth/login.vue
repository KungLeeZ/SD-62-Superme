<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import authService from '@/services/authService'

const router = useRouter()

const email = ref('')
const password = ref('')
const loading = ref(false)
const error = ref('')

const handleLogin = async () => {
  error.value = ''
  loading.value = true

  try {
    const res = await authService.login({
      email: email.value,
      password: password.value
    })

    localStorage.setItem('token', res.token)
    localStorage.setItem('user', JSON.stringify(res.user))

    router.push('/')
  } catch (e) {
    error.value = 'Email hoặc mật khẩu không đúng'
  } finally {
    loading.value = false
  }
}

const loginWithGoogle = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google'
}
</script>

<template>
  <div class="login-page d-flex align-items-center justify-content-center">

    <div class="card shadow-lg p-4 login-box">

      <h2 class="text-center fw-bold text-danger mb-4">
        SUPREME SHOES
      </h2>

      <div v-if="error" class="alert alert-danger py-2">
        {{ error }}
      </div>

      <form @submit.prevent="handleLogin">

        <div class="mb-3">
          <label class="form-label">Email</label>
          <input
              v-model="email"
              type="email"
              class="form-control"
              required
          />
        </div>

        <div class="mb-3">
          <label class="form-label">Password</label>
          <input
              v-model="password"
              type="password"
              class="form-control"
              required
          />
        </div>

        <button
            class="btn btn-danger w-100"
            :disabled="loading"
        >
          {{ loading ? 'Logging in...' : 'Login' }}
        </button>
      </form>

      <div class="text-center my-3 text-secondary">
        or
      </div>

      <button
          class="btn btn-outline-dark w-100"
          @click="loginWithGoogle"
      >
        Continue with Google
      </button>

    </div>

  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #000 0%, #111 100%);
}

.login-box {
  width: 380px;
  border-radius: 16px;
}

.form-control {
  border-radius: 10px;
}
</style>
