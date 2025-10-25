import graphene

class DonacionInformeType(graphene.ObjectType):
    categoria = graphene.String()
    eliminado = graphene.Boolean()
    total_cantidad = graphene.Int()