const {createApp} = Vue 

createApp({
    data(){
        return{
            transactions : [],
            totalBalance : 0,
            accountId : "",
            data : [],
            transactionsOrder: [],
        }
    },
    created(){
        const params = new URLSearchParams(location.search)
        this.accountId = params.get('id')
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/accounts/" + this.accountId)
            .then(response => {
                this.totalBalance = response.data.balance
                this.transactions = response.data.transaction
                this.transactionsOrder = this.transactions.sort((a,b) => b.id - a.id)
                this.debitBalance()
            })
            .catch(error => console.log(error))
        },
        logout(){
            wal.fire({
                title: 'Are you sure that you want to log out?',
                inputAttributes: {
                    autocapitalize: 'off'
                },
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
        isDebit(transaction){
            return transaction === 'DEBIT'
        },
        debitBalance(){
            for(currentTransaction of this.transactions){
                if(this.isDebit(currentTransaction.type)){
                    this.totalBalance -= currentTransaction.amount
                }
            }
            return this.totalBalance
        },
    }
}).mount("#app")