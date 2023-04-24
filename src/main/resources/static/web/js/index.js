const {createApp} = Vue 

createApp({
    data(){
        return {
            valueForm : true,
            firstName: "",
            lastName : "",
            email : "",
            password : "",
            passwordLogin : "",
            emailLogin : "",
        }
    },
    created(){
    },
    mounted(){
    },
    methods:{
        login(){
            this.valueForm = true;
        },
        register(){
            this.valueForm = false;
        },
        addClient(){
            this.postAddClient()
        },
        loginClient(){
            this.postLoginClient()
        },
        postAddClient(){
            axios.post("/api/clients",{
                firstName : this.firstName,
                lastName : this.lastName,
                email : this.email,
                password : this.password,
            },{
                headers:{
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
            .then(() =>{
                axios.post("/api/login",{
                    email : this.email,
                    password : this.password,
                },{
                    headers:{
                        'Content-Type': 'application/x-www-form-urlencoded'
                    }
                })
                .then(window.location.href="/web/pages/accounts.html")
            })
            .catch((error) =>{
                console.log(error)
            })
        },
        postLoginClient(){
            axios.post("/api/login",{
                email : this.emailLogin,
                password : this.passwordLogin,
            },{
                headers:{
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
            .then(() =>{
                window.location.href="/web/pages/accounts.html"
            })
            .catch(error =>{
                console.log(error)
            })
        }
    }

}).mount("#app")