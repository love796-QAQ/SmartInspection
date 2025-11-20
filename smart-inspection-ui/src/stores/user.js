import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
    const token = ref('')
    const userInfo = ref({})

    function setToken(newToken) {
        token.value = newToken
    }

    function setUserInfo(info) {
        userInfo.value = info
    }

    return { token, userInfo, setToken, setUserInfo }
})
