const app = Vue.createApp({
    data(){
        return{
            inputEmail:"",
            inputPassword:"",
            inputFirstName:"",
            inputLastName:"",
            password:false,
            confirmPassword:'',
            errorLogin:false,
            errorPassCreate:false,
            errorUserCreate:false,
        }
    },
    created(){
        axios.post('/api/logout').then();
    },
    methods:{
       singIn(){
        axios.post("/api/login", `email=${this.inputEmail}&password=${this.inputPassword}`,{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => {
            if(response.status==200){
                window.location.href = "./accounts.html"
            }          
        }).catch(err=>this.errorLogin = true);
       },
       createAccount(){
        if(this.inputPassword == this.confirmPassword){ 
            axios.post('/api/clients',`firstName=${this.inputFirstName}&lastName=${this.inputLastName}&email=${this.inputEmail}&password=${this.inputPassword}`,{headers:{'content-type':'application/x-www-form-urlencoded'}}).then(response => {
                if(response.status == 201){
                    this.singIn();
                }
            }).catch(error=>this.errorUserCreate = true);
        }else{
            this.errorPassCreate = true;
        } 
       },
       singOut(){
        axios.post('/api/logout').then(response => console.log(response))
       },
       seePassword(){
            this.password = !this.password
       },
       empty(){
           this.inputEmail='';
           this.inputPassword='';
           this.inputFirstName='';
           this.inputLastName='';           
       }
       

    },
});

let mount = app.mount('#app');

