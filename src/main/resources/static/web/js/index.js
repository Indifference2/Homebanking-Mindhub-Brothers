const {createApp} = Vue 

createApp({
    data(){
        return {
            valueForm : true,
            firstName: "",
            lastName : "",
            emailRegister : "",
            passwordRegister : "",
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
                email : this.emailRegister,
                password : this.passwordRegister,
            },{
                headers:{
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            })
            .then(() =>{
                let timerInterval;
                Swal.fire({
                    icon: "success",
                    title: "Success",
                    html: `Account created succesfully, you will redirect automatically in <strong></strong>`,
                    timer: 5000,
                    showConfirmButton : false,
                    didOpen: () =>{
                        const content = Swal.getHtmlContainer()
                        timerInterval = setInterval(() =>{
                            Swal.getHtmlContainer().querySelector('strong')
                                .textContent = (Swal.getTimerLeft() / 1000)
                                    .toFixed(0)
                        }, 100)
                    }
                    }
                )
                .then(()=>{
                    this.emailLogin = this.emailRegister
                    this.passwordLogin = this.passwordRegister
                    this.postLoginClient()
                })
            })
            .catch(() =>{
                Swal.fire({
                    icon: 'error',
                    title: 'Oops...',
                    text: 'Email already in use!',
                    confirmButtonColor : "#009269",
                })
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
            .catch(() =>{
                Swal.fire({
                    title: "There is a problem!",
                    text: "Email or password incorrect",
                    icon: "error",
                    confirmButtonColor : "#009269",
                })
            })
        }
    }

}).mount("#app")
