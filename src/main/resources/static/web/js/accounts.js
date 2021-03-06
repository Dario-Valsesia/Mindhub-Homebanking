const app = Vue.createApp({
    data(){
        return{
            client:[], 
            accounts:[],
            totalBalance: 0,
            darkMode:false,
            loans:[],
            numberAccount:'',
            viewModal:false,
            errorMessage:'',
            requestMessage:'',
        }
    },
    created(){
        this.loadData();
        this.darkMode = JSON.parse(localStorage.getItem('dark'));
    },
    methods:{
        loadData(){          
            axios.get('/api/clients/current')
                .then(res=>{
                    this.client = res.data;
                    this.accounts = res.data.accounts;
                    this.loans = res.data.loans;
                    this.orderId();
                    this.accounts.forEach(account=>this.totalBalance+=account.balance);
                    
                })
                .catch(e=>console.error(e))
        },
        getUrl(id){
            return `./account.html?id=${id}`
        },
        orderId(){
            this.accounts.sort((a,b)=>{
                if(a.id < b.id){return -1};
                if(a.id > b.id){return 1}
                return 0
            })
        },
        setDarkMode(){
            this.darkMode = !this.darkMode;
            localStorage.setItem("dark", JSON.stringify(this.darkMode)); 
            document.querySelector('.btn-nav-close').click();      
        },
       
        singOut(){
            axios.post('/api/logout').then(response => console.log(response))
            window.location.href = "./web/index.html"
        },
        createAccount(){
            axios.post('/api/clients/current/accounts',{headers:{'content-type':'application/x-www-form-urlencoded'}})
                .then(location.reload())
                .catch(e=>console.log(e))
        },
        confirm(e){
            this.numberAccount=e.target.value;
            this.viewModal=true;
        },
        deleteAccount(e){
            axios.delete(`/api/accounts/delete?number=${this.numberAccount}`).then(res=>{
                this.requestMessage="Account deleted";
                setTimeout(()=>location.reload(),1200);
            }).catch(e=>this.errorMessage=e.response.data);
        },
        closeModal(){   
            this.viewModal=false;
            location.reload();
        },
    },
});

let mount = app.mount('#app');