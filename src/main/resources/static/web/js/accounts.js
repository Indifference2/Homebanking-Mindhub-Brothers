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
    },
}).mount("#app")