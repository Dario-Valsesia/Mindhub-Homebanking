console.log('hola');
const app = Vue.createApp({
    data(){
        return{
            clientes:[],
            data:'',
            form:{
                nombre:'',
                apellido:'',
                email:'',
            }
            
        }
        
    },
    created(){
        this.loadClient()           
    },
    methods:{
        addClient(e){
            if(this.form.nombre != '' && this.form.apellido != '' && this.form.email != ''){
                e.preventDefault()
                axios.post('/rest/clients', {
                    firstName:this.form.nombre,
                    lastName: this.form.apellido,
                   // eMail: this.form.email,
                }).then(res=>{
                    console.log(res);
                    this.loadClient();   
                })  
            }
              
        },
        deleteClient(e){    
            axios.delete(e.target.id)
                .then(res=>{
                    this.loadClient();
                })
               
        },
        loadClient(){
            axios.get('/rest/clients')
            .then(res => {
                this.clientes = res.data._embedded.clients;
                this.data = res.data;
                this.form.nombre = '';
                this.form.apellido = '';
                this.form.email = '';
            })
        }
    },
})

let asd = app.mount('#app');