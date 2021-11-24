const app = Vue.createApp({
    data(){
        return{
            idAccount:0,     
            account:[],
            transactions:[],
            darkMode:false,
            btnDonation: false,  
            balanceDario:0,            
        }
    },
    created(){
        this.getParameterUrl();
        this.loadData();
        this.darkMode = JSON.parse(localStorage.getItem('dark'));
    },
    methods:{
        loadData(){          
            axios.get(`/api/accounts/${this.idAccount}`)
                .then(res=>{
                    this.account=res.data;
                    this.transactions=res.data.transactions;
                    this.orderTransactionsId();                   
                }); 
        },
        getParameterUrl(){
            const urlParams = new URLSearchParams(window.location.search);
            const id = urlParams.get('id');
            this.idAccount = id;
        },
        orderTransactionsId(){
            this.transactions.sort((a,b)=>{
                if(a.id < b.id){return 1};
                if(a.id > b.id){return -1};
                return 0;
            })
        },
        setDarkMode(){
            this.darkMode = !this.darkMode;
            localStorage.setItem("dark", JSON.stringify(this.darkMode) );  
            document.querySelector('.btn-nav-close').click();     
        },
        closeDonation(){
            this.btnDonation = false;
        },
        singOut(){
            axios.post('/api/logout').then(response => console.log(response))
        }
    },
});

let mount = app.mount('#app');

