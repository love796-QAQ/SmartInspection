import { defineStore } from 'pinia'

const useUserStore = defineStore('user', {
    state: () => {
        const storedUser = JSON.parse(localStorage.getItem('user')) || {}
        return {
            token: localStorage.getItem('token') || '',
            name: storedUser.realName || '',
            avatar: '',
            roles: [],
            permissions: [],
            dataScope: localStorage.getItem('dataScope') || '',
            user: storedUser,
            currentDeptId: null // For Dashboard drill-down
        }
    },
    actions: {
        // Login
        login(userInfo) {
            const { username, password } = userInfo
            return new Promise((resolve, reject) => {
                login({ username: username.trim(), password: password }).then(res => {
                    const data = res.data
                    this.token = data.token
                    this.user = data.user
                    this.name = data.user.realName
                    this.dataScope = data.dataScope
                    localStorage.setItem('token', data.token)
                    localStorage.setItem('user', JSON.stringify(data.user))
                    localStorage.setItem('dataScope', data.dataScope)
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        },

        // Logout
        logout() {
            return new Promise((resolve) => {
                this.token = ''
                this.roles = []
                this.permissions = []
                this.dataScope = ''
                this.currentDeptId = null
                localStorage.removeItem('token')
                localStorage.removeItem('user')
                localStorage.removeItem('dataScope')
                resolve()
            })
        },

        setCurrentDeptId(deptId) {
            this.currentDeptId = deptId
        }
    }
})

export default useUserStore
