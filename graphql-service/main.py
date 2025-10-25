from .resolvers import Query
import graphene

schema = graphene.Schema(query=Query)