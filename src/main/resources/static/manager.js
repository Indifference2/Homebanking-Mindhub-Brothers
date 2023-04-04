const {createApp} = Vue 

createApp({
    data(){
        return{
        data: [],
        firstName : null,
        lastName : null,
        email : null,
        }
    },
    created(){
        this.loadData()
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/clients")
            .then(response => {
                this.data = response.data._embedded
            })
            .catch(error =>{
                console.log(error)
            })
        },
        addClient(){
            this.postClient()
        },
        postClient(){
            axios.post("http://localhost:8080/clients",{
                firstName : this.firstName,
                lastName : this.lastName,
                email: this.email,
            })
            .then(() => {
                this.loadData();
            })
            .catch (error => {
                console.log(error)
            })
        },
    },
})
.mount('#app')