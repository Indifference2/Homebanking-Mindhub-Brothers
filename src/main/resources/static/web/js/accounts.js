const { createApp } = Vue;

createApp({
    data() {
        return {
            dataAccount: [],
            data: [],
            dataClientLoans: [],
            accounType: "",
            roleUser : "",
        }
    },
    created() {
        this.loadData();
        this.getRole();
    },
    methods: {
        loadData() {
            axios.get("/api/clients/current")
                .then((response) => {
                    this.dataAccount = response.data.accounts
                    this.data = response.data
                    this.dataClientLoans = response.data.clientLoans
                })
                .catch(error => console.log(error))
        },
        getRole(){
            axios.get("/api/clients/current/rol")
            .then(response =>{
                this.roleUser = response.data
            })
        },
        logout() {
            Swal.fire({
                title: 'Are you sure that you want to log out?',
                color: "white",
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor: "#009269",
                preConfirm: (login) => {
                    return axios.post('/api/logout')
                        .then(response => {
                            window.location.href = "/web/index.html"
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
        addAccount() {
            Swal.fire({
                title: 'Are you sure you want add a account?',
                input: 'radio',
                inputOptions: {
                    CURRENT: 'CURRENT',
                    SAVING: 'SAVING'
                },
                showCancelButton: true,
                confirmButtonText: 'Yes',
                color: "white",
                confirmButtonColor: "#009269",
            })
                .then(result => {
                    if (result.isConfirmed) {
                        this.accounType = result.value;
                        axios.post('/api/clients/current/accounts', `accountType=${this.accounType}`)
                            .then(() => {
                                window.location.href = "/web/pages/accounts.html"
                            })
                            .catch(error => {
                                Swal.fire({
                                    title: "There is a problem!",
                                    text: error.response.data,
                                    icon: "error",
                                    confirmButtonColor: "#009269",
                                })
                            })
                    }

                })
        },
        deleteAccount(idAccount) {
            Swal.fire({
                title: 'Are you sure that you want delete this card?',
                background: "url(../img/bg-alert.jpg)",
                color: "white",
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor: "#00845F",
                preConfirm: (login) => {
                    return axios.put("/api/clients/current/accounts?id=" + idAccount)
                        .then((response) => {
                            Swal.fire({
                                icon: "success",
                                text: "Account deleted successfully"
                            })
                                .then((response) => {
                                    window.location.href = "/web/pages/accounts.html"
                                })
                                .catch((error) => {
                                    Swal.fire({
                                        icon: "error",
                                        text: error.response.data,
                                    })
                                })
                        })
                }
            })
        },
    }
}).mount("#app")

window.onload = function () {
    $('#onload').fadeOut();
    $('body').removeClass('hidden');
};