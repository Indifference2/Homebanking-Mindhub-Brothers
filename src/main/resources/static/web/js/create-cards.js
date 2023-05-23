const {createApp} = Vue

createApp({
    data(){
        return{
            valueType : "",
            valueColor : "",
            dataClient : [],
            roleUser: "",
        }
    },
    created(){
        this.loadData();
        this.getRole();
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients/current")
            .then(response =>{
                this.dataClient = response.data
            })
        },
        getRole(){
            axios.get("/api/clients/current/rol")
            .then(response =>{
                this.roleUser = response.data
            })
        },
        logout(){
            Swal.fire({
                title: 'Are you sure that you want to log out?',
                background : "url(../img/bg-alert.jpg)",
                color: "white",
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor : "#00845F",
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
        postCreateCard(){
            axios.post("/api/clients/current/cards", 
            `color=${this.valueColor}&type=${this.valueType}`)
            .then(() =>{
                Swal.fire({
                    title: 'Success',
                    text : 'Card created successfully',
                    icon: "success",
                    timer : 2000,
                    showConfirmButton: false,
                    })
                    .then(() =>{
                        window.location.href="/web/pages/cards.html"
                    })
                })
                .catch(error =>{
                    Swal.fire({
                        title: 'Error',
                        text : error.response.data,
                        icon: 'error',
                        showConfirmButton: true,
                    })
                })
            } 
        },
    })
.mount("#app")

window.onload = function () {
    $('#onload').fadeOut();
    $('body').removeClass('hidden');
};
