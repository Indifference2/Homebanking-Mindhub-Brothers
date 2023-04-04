const {createApp} = Vue; 

createApp({
    data(){
        return{
        dataAccount : [],
        data : [],
        }
    },
    created(){
        this.loadData();
    },
    methods:{
        loadData(){
            axios.get("http://localhost:8080/api/clients/1")
            .then((response)=> {
                this.dataAccount = response.data.account
                this.data = response.data
            })
            .catch(error => console.log(error))
            },
    },
}).mount("#app")