import axios from 'axios'
import { isNotEmpty } from '@/utils/util'
import { getToken, getUsername } from '@/auth/auth'

const baseURL = 'http://localhost:8002'

const apiClient = axios.create({
    baseURL,
    timeout: 15000
})

apiClient.interceptors.request.use(
    (config) => {
        config.headers.Token = isNotEmpty(getToken()) ? getToken() : ''
        config.headers.Username = isNotEmpty(getUsername()) ? getUsername() : ''
        return config
    },
    (error) => {
      return Promise.reject(error)
    }
)

// TODO: make A000200 a constant
apiClient.interceptors.response.use(
    (res) => {
        // login/authentication expiry check
        if (res.data.code === 'A000200') {
        localStorage.removeItem('token')
        }

        // success check
        if (res.status == 0 || res.status == 200) {
            return Promise.resolve(res)
        }
        return Promise.reject(res)
    },
    (err) => {
        return Promise.reject(err)
    }
)

export default apiClient