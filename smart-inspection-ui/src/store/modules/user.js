import { defineStore } from 'pinia'

const useUserStore = defineStore('user', {
    state: () => ({
        name: 'admin', // Default to admin for now
        avatar: '',
        roles: []
    }),
    actions: {
        login(userInfo) {
            const { username, password } = userInfo
            return new Promise((resolve, reject) => {
                // Mock login for now
                this.name = username
                resolve()
            })
        }
    }
})

export default useUserStore
