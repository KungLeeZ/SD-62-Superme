import axios from 'axios'

const API = 'http://localhost:8080/api/auth'

export default {
    async login(data) {
        const res = await axios.post(`${API}/login`, data)
        return res.data
    }
}
