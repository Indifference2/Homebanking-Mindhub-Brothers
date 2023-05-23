const { createApp } = Vue

createApp({
    data() {
        return {
            transactions: [],
            totalBalance: 0,
            accountId: "",
            data: [],
            transactionsOrder: [],
            startDate: "0000-00-00",
            endDate: "0000-00-00",
            transactionsDateBetween: [],
            roleUser : "",
        }
    },
    created() {
        const params = new URLSearchParams(location.search)
        this.accountId = params.get('id')
        this.loadData()
        this.getRole()
    },
    mounted() {

    },
    computed: {
        filterDateBetween() {
            axios.get("http://localhost:8080/api/accounts/" + this.accountId + "/transactions/dateBetween", {
                params: {
                    startDate: this.startDate,
                    endDate: this.endDate,
                }
            }).then(response => {
                this.transactionsDateBetween = response.data.sort((a, b) => new Date(a.date) - new Date(b.date))
            })
                .catch(error => {
                    console.log(error)
                })
        },
    },

    methods: {
        loadData() {
            axios.get("http://localhost:8080/api/accounts/" + this.accountId + "/transactions")
                .then(response => {
                    this.totalBalance = response.data.balance
                    this.transactions = response.data
                    this.transactionsOrder = this.transactions.sort((a, b) => new Date(b.date) - new Date(a.date))
                    this.endDate = this.transactionsOrder[0].date.split('T')[0]
                    this.startDate = this.transactionsOrder[this.transactionsOrder.length - 1].date.split('T')[0]
                    this.debitBalance()
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
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                color: "white",
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
        isDebit(transaction) {
            return transaction === 'DEBIT'
        },
        debitBalance() {
            for (currentTransaction of this.transactions) {
                if (this.isDebit(currentTransaction.type)) {
                    this.totalBalance -= currentTransaction.amount
                }
            }
            return this.totalBalance
        },
        downloadPdf() {
            Swal.fire({
                title: 'Are you sure that you want to download transactions?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
                showCancelButton: true,
                color: "white",
                confirmButtonText: 'Sure',
                showLoaderOnConfirm: true,
                confirmButtonColor: "#009269",
                preConfirm: (login) => {
                    return axios.post("http://localhost:8080/api/accounts/" + this.accountId + "/transactions/dateBetween/pdf?startDate="+ this.startDate + "&endDate="+ this.endDate)

                        .then(response => {
                            Swal.fire({
                                icon: "success",
                                title: "Success",
                                text: "Downloaded succesfully",
                                showConfirmButton: true,
                            })
                        })
                        .catch(error => {
                            Swal.fire({
                                title: "There is a problem!",
                                text: error.response.data,
                                icon: "error",
                                confirmButtonColor: "#009269",
                            })
                        })
                },
                allowOutsideClick: () => !Swal.isLoading()
            })
        },
    }
}).mount("#app")

window.onload = function () {
    $('#onload').fadeOut();
    $('body').removeClass('hidden');
};