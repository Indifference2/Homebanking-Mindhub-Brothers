const {createApp} = Vue; 

createApp({
    data(){
        return{
        dataAccount : [],
        data : [],
        dataClientLoans: [],
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients/current")
            .then((response)=> {
                this.dataAccount = response.data.account
                this.data = response.data
                this.dataClientLoans = response.data.clientLoans
            })
            .catch(error => console.log(error))
            },
        logout(){
            Swal.fire({
                title: 'Are you sure that you want to log out?',
                background : "url(../img/bg-alert.jpg)",
                color: "white",
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor : "#009269",
                preConfirm: (login) => {
                    return axios.post('/api/logout')
                        .then(response => {
                            window.location.href="/web/index.html"
                        })
                        .catch(error => {
                            Swal.showValidationMessage(
                                'Request failed: ${error}'
                            )
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
        addAccount(){
            Swal.fire({
                title: 'Are you sure you want add a account?',
                showCancelButton: true,
                confirmButtonText: 'Yes',
                confirmButtonColor: "#009269",
                preConfirm: () =>{
                    return axios.post('/api/clients/current/accounts')
                    .then(() =>{
                        window.location.href="/web/pages/accounts.html"
                    })
                    .catch(error =>{
                        Swal.fire({
                            title: "There is a problem!",
                            text: error.response.data,
                            icon: "error",
                            confirmButtonColor : "#009269",
                        })
                    })
                }
            })
        }
    },
}).mount("#app")