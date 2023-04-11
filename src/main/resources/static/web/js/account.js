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